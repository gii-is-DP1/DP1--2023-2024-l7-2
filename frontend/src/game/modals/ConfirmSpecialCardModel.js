import React, { useEffect, useState, useContext } from 'react'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Input, FormGroup, Form, Label } from 'reactstrap'
import SellAnItemForm from './SpecialCardForms/SellAnItemForm';


export default function ConfirmSpecialCardModel(props) {

    const [gold, setGold] = useState(0);
    const [steal, setSteal] = useState(0);
    const [iron, setIron] = useState(0);

    let title;
    let description;
    if (props.card) {

        title = props.card.name
        description = props.card.description
    }
    console.log(props.card)
    function resolveAction(numberOfDwarves) {
        console.log("Selected "+numberOfDwarves+"dwarves")
        console.log(gold)
        console.log(steal)
        console.log(iron)
    }

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
                    {title === "Sell an item" && 
                        <SellAnItemForm 
                        gold={gold} setGold={setGold}  
                        steal={steal} setSteal={setSteal} 
                        iron={iron} setIron={setIron} 
                        />
                    }
                </ModalBody>


                <ModalFooter>
                    <Button
                        onClick={() => resolveAction(2)}>
                        <div>
                            Use two dwarves
                        </div>
                    </Button>
                    <Button
                        onClick={() => resolveAction(1)}>
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