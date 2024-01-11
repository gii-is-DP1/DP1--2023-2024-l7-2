import React, { useEffect, useState, useContext } from 'react'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader, Input, FormGroup, Form, Label } from 'reactstrap'
import tokenService from "../../services/token.service";
import useFetchState from "../../util/useFetchState";
import useIntervalFetchState from '../../util/useIntervalFetchState';


const jwt = tokenService.getLocalAccessToken();

export default function ChatModel(props) {
    const code = props.code;

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [msgToSend, setMsgToSend] = useState(null);
    const [messages, setMessages] = useIntervalFetchState(
        [],
        `/api/v1/game/play/${code}/chat`,
        jwt,
        setMessage,
        setVisible,
        code
      );

    function handleSubmit(event) {

        event.preventDefault();

        let tmp_msg = {
            "text": msgToSend
        }

        fetch(
            `/api/v1/game/play/${code}/chat`,
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(tmp_msg),
            }
        ).then((tmp) => setMsgToSend(""))
        .catch((message) => alert(message));
    }

    let messages_display = messages.map((msg) => {
        if (props.player && msg.sender.username == props.player.name) {
            return (
                <div style={{textAlign:"right"}}>{msg.sender.username} - {msg.text}</div>
            );
        }
        return (
            <div>{msg.sender.username} - {msg.text}</div>
        );
    })

    return (
        <Modal
        toggle={props.toggle}
        isOpen={props.isOpen}>
            <ModalHeader style={{textAlign:"center"}}>
                Chat
            </ModalHeader>
            {props.children}

            <ModalBody>
                {messages_display}
            </ModalBody>

            <ModalFooter>
                <Input
                    type="text"
                    required
                    name="message"
                    id="message"
                    value={msgToSend || ""}
                    onChange={(e) => setMsgToSend(e.target.value)}
                    className="custom-input"
                />

                <Button onClick={handleSubmit}>
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