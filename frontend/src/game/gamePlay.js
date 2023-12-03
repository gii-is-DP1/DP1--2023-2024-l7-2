import { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import getErrorModal from "../util/getErrorModal";
import { Button, ButtonGroup, Table } from "reactstrap";
import getIdFromUrl from "./../util/getIdFromUrl";
import Card from "./../cards/card"
import SpecialCard from "../cards/specialCard";
import  { fetchDwarves, fetchCards, fetchPlayers, 
  fetchIsMyTurn, isFinished, sendCard, isStart, specialOrder}  from "./gameFunctions";
import ConfirmSpecialCardModel from "./modals/ConfirmSpecialCardModel";
import '../static/css/game/objects.css'; 


const jwt = tokenService.getLocalAccessToken();
const user = tokenService.getUser()

export default function GamePlay() {
  const code = getIdFromUrl(2);
  //console.log(jwt)


  const [gameStarted, setGameStarted] = useState(null);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [choosedCard, setChoosedCard] = useState(null);
  const [choosedSpecialCard, setChoosedSpecialCard] = useState(null);
  const [specialCardToBeConfirmed, setSpecialCardToBeConfirmed] = useState(false);
  const [cards, setCards] = useFetchState(
    [],
    `/api/v1/game/play/${code}/getCards`,
    jwt,
    setMessage,
    setVisible,
    code
  );
  const [specialCards, setSpecialCards] = useFetchState(
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

  const emptySelectedSpecialCards = {1: null,2: null,3: null}
  const [selectedSpecialCards,setSelectedSpecialCards] = useState(emptySelectedSpecialCards)

  const [game, setGame] = useFetchState(
      {},
      `/api/v1/game/play/${code}`,
      jwt,
      setMessage,
      setVisible,
      code
  );

  const [players, setPlayers] = useFetchState(
    [],
    `/api/v1/game/play/${code}/players`,
    jwt,
    setMessage,
    setVisible,
    code
  );
  const player = players.filter(p => p.name === user.username)[0];

  const [isMyTurn, setIsMyTurn] = useFetchState(
    false,
    `/api/v1/game/play/${code}/isMyTurn`,
    jwt,
    setMessage,
    setVisible,
    code
  );

  const [dwarves, setDwarves] = useFetchState([]);
  
  useEffect(() => {
    setChoosedCard(null)
  },[])

  const modal = getErrorModal(setVisible, visible, message);


  
  function selectCard(id,card) {
    if (isMyTurn === false) {
      console.log("is not your turn")
      return false; // Just a random return to ensure that function exits
    }

    if (selectedCards[id] !== null && selectedCards[id] !== undefined) {
      // Card is already selected, you can't select it
      console.log(selectedCards)
      console.log(id);
      console.log(selectedCards[id])
      return false;
    }
    
    if (choosedCard === card) {
      //console.log("we are filtering")
      //setChoosedCard(choosedCard.filter((c) => c.position !== card.position))
      setChoosedCard(null);
    } else {
      if (choosedCard === null) {
        setChoosedCard(card)
      } else {
        console.log("You cant choose that many cads :(")
      }
    }
  }

  function selectSpecialCard(id,specialCard) {
    console.log("we are here")
    if (isMyTurn === false) {
      console.log("is not your turn")
      return false; // Just a random return to ensure that function exits
    }
    setSpecialCardToBeConfirmed(true);
    setChoosedSpecialCard(specialCard)
  }


  //console.log(selectedCards)
  function getCardColor(id,card) {
    // If card was already selected by another player, card's color is other player's color
    let color = selectedCards[id] !== null ? selectedCards[id] : "white"

    // Else it is checked if the card has been selected
    if ( color === "white") {
      color = choosedCard === card ? player.color : "white"
    }

    return color
  }
/*
  function specialOrderHandler() {
    specialOrder(code, jwt, setSelectedCards);
  }
  const isSpecialOrderCard = (specialCard) => {
    // Replace 'isSpecialOrderCard' with the actual property or condition that identifies the "Special Order" card
    return specialCard  === "Special Order";
  };*/

  const playerList = players.map((play) => {
    return (
      <tr key={play.id} style={{ color: play.color }}>
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
      </tr>
    )
  })

  const buttonStartGame = () => {
    setGameStarted(true);
    isStart();
  };

  //console.log(choosedCard)
  return (
    <>
    <ConfirmSpecialCardModel
      isOpen={specialCardToBeConfirmed}
      toggle={() => {
        setSpecialCardToBeConfirmed(!specialCardToBeConfirmed)
      }}
      card={choosedSpecialCard}
    ></ConfirmSpecialCardModel>
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

          { (gameStarted || game != {} && game.playerCreator && game.playerCreator.name !== user.username) &&
        <section className="buttonsLayout" style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>

            <Button
                onClick={() => {isFinished(code, jwt)}}
                title="Finish?"
                color="#008000"
                style={{border: '3px solid black',padding: "3px"}}>
                  Finish?
            </Button>
          
            <Button
                onClick={() => {fetchCards(code, jwt, cards, setCards)}}
                title="Finish?"
                color="#008000"
                style={{border: '3px solid black',padding: "3px"}}>
                  Get Cards
            </Button>

            <Button
              onClick={() => {fetchIsMyTurn(game, code, jwt, isMyTurn, setIsMyTurn, setSelectedCards, 
                setChoosedCard, setGame, setDwarves)}}
              title="Start Game"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}>
              checkTurn
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
            <Button
              onClick={() => {
                fetchPlayers(code, jwt, players, setPlayers)
              }}
              title="Fetch Players"
              color="#008000"
              style={{ border: "3px solid black", padding: "3px" }}
            >
              Fetch players
            </Button>
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
          {specialCards.length != 0 && player && player.color &&
          <section className="specialCardDeckLayout"  style={{display:"flex", flexDirection:"row", gap:"40px", margin:"40px"}}>
            <SpecialCard id={specialCards[0].id} 
                  onClick={() => selectSpecialCard(1,specialCards[0])}/>
            <SpecialCard id={specialCards[1].id}
                  onClick={() => selectSpecialCard(2,specialCards[1])}/>
            <SpecialCard id={specialCards[2].id} 
                  onClick={() => selectSpecialCard(3,specialCards[2])}/>
          </section>
          }
        </section>
      </div>
    </div>
    </>
  );


}
