import React, { useEffect, useState, useContext } from 'react'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Input, FormGroup, Form, Label } from 'reactstrap'
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";


const jwt = tokenService.getLocalAccessToken();

export default function ChatModel(props) {


    return (
        <Modal
        toggle={props.toggle}
        isOpen={props.isOpen}>
            <ModalHeader style={{textAlign:"center"}}>
                Chat
            </ModalHeader>
            {props.children}

            <ModalFooter>
                <Input
                    type="text"
                    required
                    name="message"
                    id="message"
                    //value={ || ""}
                    //onChange={}
                    className="custom-input"
                />

                <Button>
                    <div>
                        Send
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