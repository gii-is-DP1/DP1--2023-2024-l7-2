package org.springframework.samples.petclinic.game;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.dwarf.Dwarf;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    GameRepository gr;
    PlayerRepository pr;

    @Autowired
    public GameService(GameRepository gr, PlayerRepository pr) {
        this.gr = gr;
        this.pr = pr;

    }

    @Transactional(readOnly = true)
    public List<Game> getAllGames() {
        return gr.findAll();
    }

    @Transactional(readOnly = true)
    public List<Game> getWaitingGames() {
        return gr.findByStart(null);
    }

    @Transactional
    public Game saveGame(Game g) {
        return gr.save(g);
    }

    @Transactional
    public Optional<Game> getGameById(Integer id) {
        return gr.findById(id);
    }

    @Transactional
    public Optional<List<Player>> getPlayers(Integer id) {
        return gr.getPlayersByGameId(id);
    }

    @Transactional(readOnly = true)
    public Game getGameByCode(String code) {
        List<Game> games = gr.findByCode(code);
        return games.isEmpty() ? null : games.get(0);
    }

    @Transactional(readOnly = true)
    public List<Game> getGamesLike(String pattern) {
        return gr.findByNameContains(pattern);
    }

    @Transactional(readOnly = true)
    public List<Game> getFinishedGames() {
        return gr.findByFinishIsNotNull();
    }

    @Transactional(readOnly = true)
    public List<Game> getOngoingGames() {
        return gr.findByFinishIsNullAndStartIsNotNull();
    }

    @Transactional
    public void delete(Integer id) {
        gr.deleteById(id);
    }

    @Transactional
    public Player getGameWinner(Game g) {

        // Hacemos que la lista de players cambie de Optional<List<Player>> a
        // List<Player>

        Optional<List<Player>> plys_optional = getPlayers(g.getId());
        List<Player> plys = plys_optional.get();

        Map<Player, Integer> totalScore = new HashMap<Player, Integer>();
        plys.stream().forEach(p -> totalScore.put(p, 0));

        // steal
        Player playerWithMoreSteal = null;

        for (Player p : plys) {
            if (p.getSteal() == 0) {
                continue;
            }

            if (playerWithMoreSteal == null && p.getSteal() > 0) {
                playerWithMoreSteal = p;
                continue;
            }
            if (p.getSteal() > playerWithMoreSteal.getSteal()) {
                playerWithMoreSteal = p;
            }
        }
        if (playerWithMoreSteal != null) {
            totalScore.put(playerWithMoreSteal, totalScore.get(playerWithMoreSteal) + 1);
        }

        // gold

        Player playerWithMoreGold = null;
        for (Player p : plys) {
            if (p.getGold() == 0) {
                continue;
            }
            if (playerWithMoreGold == null && p.getGold() > 0) {
                playerWithMoreGold = p;
                continue;
            }
            if (p.getGold() > playerWithMoreGold.getGold()) {
                playerWithMoreGold = p;
            }
        }
        if (playerWithMoreGold != null)
            totalScore.put(playerWithMoreGold, totalScore.get(playerWithMoreGold) + 1);

        Integer mayor = totalScore.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue)).get().getValue();

        // iron
        if (hayEmpate(totalScore.values().stream().toList(), mayor) == true) {
            Player playerWithMoreIron = null;
            Integer m = mayor;
            List<Player> plys1 = totalScore.entrySet().stream().filter(i -> i.getValue() == m).map(i -> i.getKey())
                    .toList();
            for (Player p : plys1) {
                if (p.getIron() == 0) {
                    continue;
                }
                if (playerWithMoreIron == null && p.getIron() > 0) {
                    playerWithMoreIron = p;
                    continue;
                }
                if (p.getIron() > playerWithMoreIron.getIron()) {
                    playerWithMoreIron = p;
                }
            }
            if (playerWithMoreIron != null) {
                totalScore.put(playerWithMoreIron, totalScore.get(playerWithMoreIron) + 1);
            }
        }

        mayor = totalScore.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue)).get().getValue();

        // medal
        if (hayEmpate(totalScore.values().stream().toList(), mayor) == true) {
            Player playerWithMoreMedal = null;
            Integer m = mayor;
            List<Player> plys2 = totalScore.entrySet().stream().filter(i -> i.getValue() == m).map(i -> i.getKey())
                    .toList();
            for (Player p : plys2) {
                if (p.getMedal() == 0) {
                    continue;
                }
                if (playerWithMoreMedal == null && p.getMedal() > 0) {
                    playerWithMoreMedal = p;
                    continue;
                }
                if (p.getIron() > playerWithMoreMedal.getIron()) {
                    playerWithMoreMedal = p;
                }
            }
            if (playerWithMoreMedal != null) {
                totalScore.put(playerWithMoreMedal, totalScore.get(playerWithMoreMedal) + 1);
            }
        }

        Player p = totalScore.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(null);

        return p;
    }

    private Boolean hayEmpate(List<Integer> lista, Integer mayor) {
        Integer cont = 0;
        for (int i : lista) {
            if (lista.get(i).equals(mayor)) {
                cont++; // Hay elementos duplicados
            }
        }
        return cont >= 2;
    }

    @Transactional
    public void updateMaterials(Game g) {
        // Comprobamos si hay un dwarf por cada jugador en esa ronda
        List<Dwarf> dwarves = g.getDwarves();
        dwarves = dwarves.stream().filter(d -> d.getRound() == g.getRound()).toList();

        Optional<List<Player>> plys_optional = getPlayers(g.getId());
        List<Player> plys = plys_optional.get();

        if (dwarves.size() == plys.size()) {
            // Añadimos a cada player los materiales correspondientes a las carta elegidas
            for (int i = 0; i < dwarves.size(); i++) {
                Player p = dwarves.get(i).getPlayer();
                Dwarf d = dwarves.get(i);
                List<Card> c = d.getCards();

                for (int j = 0; j < c.size(); j++) {
                    Card card = c.get(j);
                    System.out.println(card.getTotalIron());
                    System.out.println(card.getCardType());
                    if (card.getCardType().getName().equals("Other")) {
                        p.setGold(p.getGold() + card.getTotalGold());
                        p.setIron(p.getIron() + card.getTotalIron());
                        p.setSteal(p.getSteal() + card.getTotalSteal());
                        p.setMedal(p.getMedal() + card.getTotalMedals());
                    }
                }
                System.out.println(p.getIron());
                pr.save(p);
            }

        }
    }

}