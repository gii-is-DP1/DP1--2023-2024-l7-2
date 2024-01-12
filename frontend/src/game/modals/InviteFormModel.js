import React, { useEffect, useState } from 'react'
import {Button, Modal, ModalFooter, ModalHeader, Input } from 'reactstrap'
import tokenService from "../../services/token.service";

const jwt = tokenService.getLocalAccessToken();

export default function InviteFormModel(props) {
    const code = props.code;

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [invitation, setInvitation] = useState(null);

    function handleSubmit(event) {
        event.preventDefault();

        fetch(
            `/api/v1/game/play/${code}/invite`,
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: invitation,
            })
            .then((response) => response.json())
            .then((json) => {
                setMessage(json.message);
                setVisible(true);
            })
        .catch((message) => alert(message));
        props.toggle();
    }
    
    return (
        <Modal
        toggle={props.toggle}
        isOpen={props.isOpen}>
            <ModalHeader style={{textAlign:"center"}}>
                Invitation
            </ModalHeader>

            <ModalFooter>
                <Input
                    type="text"
                    required
                    name="username"
                    id="username"
                    value={invitation || ""}
                    onChange={(e) => setInvitation(e.target.value)}
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