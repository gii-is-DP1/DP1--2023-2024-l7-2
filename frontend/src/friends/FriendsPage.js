import React, { useState, useEffect } from "react";
import tokenService from "../services/token.service";

const FriendList = () => {
 const [friends, setFriends] = useState([]);
 const jwt = tokenService.getLocalAccessToken();

 useEffect(() => {
 fetch("/api/v1/friends", {
   headers: {
     Authorization: `Bearer ${jwt}`,
   },
 })
   .then((response) => {
     if (response.ok) {
       return response.json();
     } else {
       throw new Error("Error fetching friends");
     }
   })
   .then((data) => {
     setFriends(data);
   })
   .catch((error) => {
     console.error("Error:", error);
   });
 }, []);

 const rectanguloStyle = {
  width: '25%',
  height: '400px',
  backgroundColor: 'white',
  border: '3px solid black',
 };

 return (
 <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'space-between' }}>
   {friends.map((friend) => (
     <div key={friend.id} style={rectanguloStyle}>
       <h1 style={{ textAlign: 'center' }}> {friend.name} </h1>
     </div>
   ))}
 </div>
 );
};

export default FriendList;
