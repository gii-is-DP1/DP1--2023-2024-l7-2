import React, { useEffect, useState, useContext } from 'react'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Input, FormGroup, Form, Label } from 'reactstrap'
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import {FormSellAnItem, resolveSellAnItem} from './SpecialCardForms/SellAnItemForm';
import {FormSpecialOrder, resolveSpecialOrder} from './SpecialCardForms/SpecialOrderForm';
import {FormPastGlories, resolvePastGlories} from './SpecialCardForms/PastGloriesForm';
import {FormTurnBack, resolveTurnBack} from './SpecialCardForms/TurnBackForm';

const jwt = tokenService.getLocalAccessToken();

export default function ConfirmSpecialCardModel(props) {

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    const [gold, setGold] = useState(0);
    const [steal, setSteal] = useState(0);
    const [iron, setIron] = useState(0);
    const [objectSelected, setObjectSelected] = useState(null);
    const [position, setPosition] = useState(1);
    const [cardSelected, setCardSelected] = useState(null);

    const [gameObjects, setGameObjects] = useFetchState(
        [],
        `/api/v1/objects`,
        jwt,
        setMessage,
        setVisible,
        ""
      );
    
    

    let title;
    let description;
    if (props.card) {
        title = props.card.name
        description = props.card.description
    }

    useEffect(() => {
        if (props.playerObjects && gameObjects) {
            let tmp_gameObjects = gameObjects
            for (const i in props.playerObjects) {
                tmp_gameObjects = tmp_gameObjects.filter((obj) => obj.name !== props.playerObjects[i].name)
            }
            setGameObjects(tmp_gameObjects)
        }
    }, [props.playerObjects])

    function resolveAction(numberOfDwarves) {
        console.log("Selected "+numberOfDwarves+"dwarves")
        
        let objectToSend
        switch (title) {
            case "Special order":
                console.log("Special order selected")
                console.log(gameObjects)
                console.log(objectSelected)

                for (const i in gameObjects) {
                    if (gameObjects[i].name === objectSelected){
                        objectToSend = gameObjects[i]
                        //setObjectSelected(gameObject[i])
                        break;
                    }
                }
                
                resolveSellAnItem(props.code, jwt, {
                    specialCard: props.card,
                    usesBothDwarves: numberOfDwarves === 2,
                    selectedGold: gold,
                    selectedIron: iron,
                    selectedSteal: steal,
                    selectedObject: objectToSend
                })
                break;
            case "Sell an item":
                console.log("sell an item selected")
                console.log(gameObjects)
                console.log(objectSelected)

                for (const i in gameObjects) {
                    if (gameObjects[i].name === objectSelected){
                        objectToSend = gameObjects[i]
                        //setObjectSelected(gameObject[i])
                        break;
                    }
                }
                
                resolveSellAnItem(props.code, jwt, {
                    specialCard: props.card,
                    usesBothDwarves: numberOfDwarves === 2,
                    selectedGold: gold,
                    selectedIron: iron,
                    selectedSteal: steal,
                    selectedObject: objectToSend
                })
                break;
            case "Past Glories":
                console.log("Past Glories selected")
                console.log(position)
                console.log(cardSelected)
                resolvePastGlories(props.code,jwt, {
                    specialCard: props.card,
                    usesBothDwarves: numberOfDwarves === 2,
                    position: position,
                    pastCard: cardSelected

                })
                break
            case "Turn back":
                console.log("Turn back selected")
                console.log(position)
                resolveTurnBack(props.code,jwt, {
                    specialCard: props.card,
                    usesBothDwarves: numberOfDwarves === 2,
                    position: position
                })
                break
            default:
                resolveSellAnItem(props.code,jwt, {
                    specialCard: props.card,
                    usesBothDwarves: numberOfDwarves === 2,
                })
        }
    }

    /* 
        Las cartas que necesitan un 
        formulario especial son Turn Back, Special Order,
        Past Glories y Sell an Item 
    */
    return (
        <Modal
        toggle={props.toggle}
        isOpen={props.isOpen}>

                <ModalHeader style={{textAlign:"center"}}>
                    {title}
                </ModalHeader>
                {props.children}

                <ModalBody>
                    {description}
                    {title === "Special order" && 
                        <FormSpecialOrder
                        gold={gold} setGold={setGold}  
                        steal={steal} setSteal={setSteal} 
                        iron={iron} setIron={setIron}
                        objectSelected={objectSelected} setObjectSelected={setObjectSelected}
                        gameObject={gameObjects} setGameObject={setGameObjects}
                        />
                    }
                    {title === "Sell an item" && 
                        <FormSellAnItem 
                        gold={gold} setGold={setGold}  
                        steal={steal} setSteal={setSteal} 
                        iron={iron} setIron={setIron}
                        objectSelected={objectSelected} setObjectSelected={setObjectSelected}
                        gameObject={gameObjects} setGameObject={setGameObjects}
                        />
                    }
                    {title == "Past Glories" && 
                        <FormPastGlories
                            position={position} setPosition={setPosition}
                            cardSelected={cardSelected} setCardSelected={setCardSelected}
                        />
                    }
                    {title == "Turn back" && 
                        <FormTurnBack
                            position={position} setPosition={setPosition}
                        />
                    }
                </ModalBody>


                <ModalFooter>
                    <Button
                        onClick={() => {resolveAction(2);props.toggle()}}>
                        <div>
                            Use two dwarves
                        </div>
                    </Button>
                    <Button
                        onClick={() => {resolveAction(1); props.toggle()}}>
                        <div>
                            Use one dwarf
                        </div>
                    </Button>
                    <Button onClick={props.toggle}>
                        <div>
                            Cancel
                        </div>
                    </Button>
                </ModalFooter>

        </Modal>
    )
}

const styles = {
    centeredView: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      marginTop: 22
    },
    modalView: {
      margin: 20,
      backgroundColor: 'white',
      borderRadius: 20,
      padding: 35,
      alignItems: 'center',
      shadowOffset: {
        width: 0,
        height: 2
      },
      shadowOpacity: 0.75,
      shadowRadius: 4,
      elevation: 5,
      width: '90%'
    },
    actionButton: {
      borderRadius: 8,
      height: 40,
      marginTop: 12,
      margin: '1%',
      padding: 10,
      alignSelf: 'center',
      flexDirection: 'column',
      width: '50%'
    },
    text: {
      fontSize: 16,
      color: 'white',
      alignSelf: 'center',
      marginLeft: 5
    },
    container: {
      flex: 1
    }
  }