import { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table } from "reactstrap";
import getIdFromUrl from "./../util/getIdFromUrl";
import Card from "./../cards/card"
import SpecialCard from "../cards/specialCard";
import  { isFinished, sendCard, isStart, resign, checkResourcesSelectCard}  from "./gameFunctions";
import ConfirmSpecialCardModel from "./modals/ConfirmSpecialCardModel";
import ChatModel from "./modals/ChatModel";
import InviteFormModel from "./modals/InviteFormModel";
import useIntervalFetchState from "../util/useIntervalFetchState";

import '../static/css/game/objects.css'; 



const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser()

export default function GamePlay() {
  const code = getIdFromUrl(2);

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);

  const [myDwarves, setMyDwarves] = useState([]);
  const [gameStarted, setGameStarted] = useState(null);
  const [gameRound, setGameRound] = useState(null);
  const [choosedCard, setChoosedCard] = useState(null);
  const [choosedSpecialCard, setChoosedSpecialCard] = useState(null);
  const [specialCardToBeConfirmed, setSpecialCardToBeConfirmed] = useState(false);
  const [showChat,setShowChat] = useState(false);
  const [showInviteForm, setShowInviteForm] = useState(false)
  const [cards, setCards] = useFetchState(
    [],
    `/api/v1/game/play/${code}/getCards`,
    jwt,
    setMessage,
    setVisible,
    code
  );
  const [specialCards, setSpecialCards] = useIntervalFetchState(
    [],
    `/api/v1/game/play/${code}/getSpecialCards`,
    jwt,
    setMessage,
    setVisible,
    code
  );

  const emptySelectedCards = {1: null,2: null,3: null,
    4: null,5: null,6: null,
    7: null,8: null,9: null}
  const [selectedCards,setSelectedCards] = useState(emptySelectedCards)

  const [game, setGame] = useIntervalFetchState(
      {},
      `/api/v1/game/play/${code}`,
      jwt,
      setMessage,
      setVisible,
      code
  );

  const [players, setPlayers] = useIntervalFetchState(
    [],
    `/api/v1/game/play/${code}/players`,
    jwt,
    setMessage,
    setVisible,
    code
  );
  
  let player = players.filter(p => p.name === user.username)[0];
  if (player == null) player = {};

  const [isMyTurn, setIsMyTurn] = useState(false);
  const [playerTurn, setPlayerTurn] = useIntervalFetchState(
    null,
    `/api/v1/game/play/${code}/isMyTurn`,
    jwt,
    setMessage,
    setVisible,
    code
  );

  
  const [dwarves, setDwarves] = useIntervalFetchState(
    [],
    `/api/v1/game/play/${code}/dwarves`,
    jwt,
    setMessage,
    setVisible,
    code
  );


  const modal = getErrorModal(setVisible, visible, message);

  useEffect(()=>{
    if(playerTurn && playerTurn.name && player.name) {
      if (player.name === playerTurn.name) {
        if (!isMyTurn){
          setIsMyTurn(true)
        }
      } else {
        if (isMyTurn){
          setIsMyTurn(false)
        }
      }
    } 
  },[playerTurn])

  useEffect(() => {
    let updated = {1: null,2: null,3: null,
      4: null,5: null,6: null,
      7: null,8: null,9: null};
    
    let updatedDwarves = [];
    for (const d of dwarves) {
        if (!d.player) continue;
        const c = d.card;
        if (c==null) {
          continue;
        }
        if (d.player.name === player.name) {
          updatedDwarves.push(d);
        }

        const pacolor = d.player.color;
        //console.log(c.id + " to color => " + pacolor);
        updated[c.position] = pacolor;  
    }

    if (game.start) {
      setGameStarted(true)
    }

    setMyDwarves(updatedDwarves)
    setSelectedCards(updated)

    if (game.round !== gameRound) {
      setGameRound(game.round)
    } 
    isFinished(code, jwt);
  }, [game,dwarves])

  useEffect(() => {
    fetch(`/api/v1/game/play/${code}/getCards`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {
        if (cards.length !== response.length) return;
        for(const cardResponse in response) {

          if (response[cardResponse].id != cards[cardResponse].id) {
            setCards(response);
            break;
          }
        }
    })
  }, [playerTurn, specialCardToBeConfirmed])
  

  function selectCard(id,card) {
    if (isMyTurn === false) {
      setMessage("It is not your turn");
      setVisible(true);
      return false; // Just a random return to ensure that function exits
    }

    if (selectedCards[id] !== null && selectedCards[id] !== undefined) {
      // Card is already selected, you can't select it
      setMessage("Card already selected");
      setVisible(true);
      return false;
    }

    let cards= []
    for (const dwarf in myDwarves) {
      cards.push(myDwarves[dwarf].card)
    }
    cards.push(card)

    console.log(cards)

    if (!checkResourcesSelectCard(cards,player)) {
      setMessage("You don't have enough resources to select this card");
      setVisible(true);
      return false;
    }
    
    if (choosedCard === card) {
      setChoosedCard(null);
    } else {
      if (choosedCard === null) {
        setChoosedCard(card)
      } else {
        setMessage("You cant choose more many cads");
        setVisible(true);
      }
    }
  }

  function selectSpecialCard(id,specialCard) {
    if (!gameStarted) {
      setMessage("Game has not started yet");
      setVisible(true);
      return false; // Just a random return to ensure that function exits
    }

    if (isMyTurn === false) {
      setMessage("It is not your turn");
      setVisible(true);
      return false; // Just a random return to ensure that function exits
    }
    setSpecialCardToBeConfirmed(true);
    setChoosedSpecialCard(specialCard)
  }

  function getCardColor(id,card) {
    // it is checked if the card has been selected
    let color = choosedCard === card ? player.color : "white"
    
    // Else if card was already selected by another player, card's color is other player's color
    if ( color === "white") {
      color = selectedCards[id] !== null ? selectedCards[id] : "white"
      
    }

    return color
  }

  const playerList = players.map((play) => {
    return (
      <tr key={play.id} style={{ color: play.color, backgroundColor: "grey" }}>
        <td style={{ color: play.color }} className="text-center">{play.name}</td>
        <td style={{ color: play.color }} className="text-center">Iron: {play.iron}</td>
        <td style={{ color: play.color }} className="text-center">Gold: {play.gold}</td>
        <td style={{ color: play.color }} className="text-center">Steal: {play.steal}</td>
        <td style={{ color: play.color }} className="text-center">Medals: {play.medal}</td>
        <td style={{ color: play.color }} className="text-center">Objects: {play.objects.map(
          (object) => {
            return (
              <div className={object.name}>
                <br></br>
              </div>
            )
          }
        )}</td>
        {game.playerStart && play.name === game.playerStart.name && 
            <td style={{ color: "red" }} >Round Starter</td>
        }        
        {playerTurn && playerTurn.name && play.name === playerTurn.name && 
            <td style={{ color: "red" }} >Current turn</td>
        }
      </tr>
    )
  })
  const buttonStartGame = () => {
    setGameStarted(true);
    isStart(code,jwt);
  };

  return (
    <>
    <ConfirmSpecialCardModel
      isOpen={specialCardToBeConfirmed}
      toggle={() => {
        setSpecialCardToBeConfirmed(!specialCardToBeConfirmed)
      }}
      card={choosedSpecialCard}
      code={code}
      playerObjects={player.objects}
      selectedCards={selectedCards}
      myDwarves={myDwarves}
      player={player}
      setMessage={setMessage}
      setVisible={setVisible}
    ></ConfirmSpecialCardModel>

    <ChatModel
      isOpen={showChat}
      toggle={() => {
        setShowChat(!showChat)
      }}
      code={code}
      player={player}
    ></ChatModel>

    <InviteFormModel
      isOpen={showInviteForm}
      toggle={() => {
        setShowInviteForm(!showInviteForm)
      }}
      code={code}
    ></InviteFormModel>

    {getErrorModal(setVisible,visible,message)}
    <div style={{marginTop: "70px"}}>

      <div className="admin-page-container">
        <h1 className="text-center">Game: {game.name} - Round: {game.round}</h1>


        { game != {} && game.playerCreator && game.playerCreator.name === user.username 
          && players && players.length > 1 && !gameStarted &&
          <Button
            onClick={() => {buttonStartGame()}}
            title="Start game"
            color="#008000"
            style={{border: '3px solid black',padding: "3px"}}>
              Start game
          </Button>
        }

        { game != {} && game.playerCreator && game.playerCreator.name === user.username 
          && players && players.length < 3 && !gameStarted &&
          <Button
            onClick={() => {setShowInviteForm(!showInviteForm)}}
            title="Send Invitation"
            color="#008000"
            style={{border: '3px solid black',padding: "3px"}}>
              Send Invitation
          </Button>
        }

        <Button 
          onClick={() => {
            setShowChat(!showChat)}}
          title="showChat"
          color="#008000"
          style={{border: '3px solid black',padding: "3px"}}>
          Show Chat
        </Button>
        
        
        { (gameStarted || game != {} && game.playerCreator && game.playerCreator.name !== user.username) &&
        <section className="buttonsLayout" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>


            <Button
              onClick={() => {
                let confirmMessage = window.confirm("Are you sure you want resing? You will loose all your cards and your points");
                if(confirmMessage) resign(code, jwt);}}
              title="Resign"
              color="#008000"
              style={{border: '3px solid black',padding: "3px"}}>
              Resign
            </Button>
          
            {choosedCard && (
              <Button
              onClick={() => {
                sendCard(code,jwt,choosedCard)
                setChoosedCard(null)
              }}
              title="Send Card"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}>
                Send Card
              </Button>
            )}

          { isMyTurn && <h2>Is your turn!</h2>}
          

        </section>
          }

        <section className="generalLayout" style={{display:"flex", flexDirection:"column"}}>
          <section>
            <Table>
              <tbody>
              {playerList}
              </tbody>
            </Table>
          </section>
          {cards.length != 0 && player && player.color &&
          <section className="cardDeckLayout">
            <section className="cardDeckLayoutRow1" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[0].id} 
                    onClick={() => selectCard(1,cards[0])} color={getCardColor(1,cards[0])}/>
              <Card id={cards[1].id}
                    onClick={() => selectCard(2,cards[1])} color={getCardColor(2,cards[1])}/>
              <Card id={cards[2].id} 
                    onClick={() => selectCard(3,cards[2])} color={getCardColor(3,cards[2])}/>
            </section>
            <section className="cardDeckLayoutRow2" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[3].id}
                    onClick={() => selectCard(4,cards[3])} color={getCardColor(4,cards[3])}/>
              <Card id={cards[4].id} 
                    onClick={() => selectCard(5,cards[4])} color={getCardColor(5,cards[4])}/>
              <Card id={cards[5].id} 
                    onClick={() => selectCard(6,cards[5])} color={getCardColor(6,cards[5])}/>
            </section>
            <section className="cardDeckLayoutRow3" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
              <Card id={cards[6].id} 
                    onClick={() => selectCard(7,cards[6])} color={getCardColor(7,cards[6])}/>
              <Card id={cards[7].id} 
                    onClick={() => selectCard(8,cards[7])} color={getCardColor(8,cards[7])}/>
              <Card id={cards[8].id} 
                    onClick={() => selectCard(9,cards[8])} color={getCardColor(9,cards[8])}/>
            </section>
          </section>
          }

          <section className="specialCardDeckLayout"  style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
            {specialCards && specialCards[0] &&
              <SpecialCard id={specialCards[0].id} 
                    onClick={() => selectSpecialCard(1,specialCards[0])}/>
            }
            {specialCards && specialCards[1] &&
              <SpecialCard id={specialCards[1].id}
                    onClick={() => selectSpecialCard(2,specialCards[1])}/>
            }
            {specialCards && specialCards[2] &&
              <SpecialCard id={specialCards[2].id} 
                    onClick={() => selectSpecialCard(3,specialCards[2])}/>
            }
          </section>
          
        </section>
      </div>
    </div>
    </>
  );


}
