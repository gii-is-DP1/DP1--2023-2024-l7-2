  function finDelJuego(code,jwt) {
    fetch(`/api/v1/game/play/${code}/finish`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    })
    .then(() => {
      window.location.href = `/game/${code}/finish`;
    })
  }

  function isFinished(code, jwt) {
    fetch(`/api/v1/game/play/${code}/isFinished`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => response.json()).then(response => {
        if (response === true) {
            finDelJuego(code,jwt)
        }
    })
  }

  function isStart(code, jwt) {
    fetch(`/api/v1/game/play/${code}/isStart`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: 'application/json',
      }
    }).then(response => {
        console.log(response)
    })
  }
  
function sendCard(code, jwt, choosedCard) {
  fetch(
    "/api/v1/game/play/" + code + "/dwarves",
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json"
      },
      body: JSON.stringify(choosedCard)
    }
  ).then((response) => response.text())
    .then((data) => console.log(data))
    .catch((message) => alert(message));
}
/*
function specialOrder(code, jwt, setSelectedCards) {
  const gold = prompt("Enter the number of gold:");
  const steal = prompt("Enter the number of steal:");
  const iron = prompt("Enter the number of iron:");

  const order = {
    gold: parseInt(gold),
    steal: parseInt(steal),
    iron: parseInt(iron),
  };

  const wantsObject = window.confirm("Do you want to select an object in exchange for the materials?");
  
  if (wantsObject) {
    // Fetch the list of available objects from the server
    fetch(`/api/v1/game/play/${code}/availableObjects`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((objects) => {
        // Let the player choose an object
        const selectedObject = prompt("Select an object:\n" + objects.join("\n"));

        // Attach the selected object to the order
        order.selectedObject = selectedObject;

        // Continue with the API call
        sendOrderToServer(code, jwt, order, setSelectedCards);
      })
      .catch((error) => {
        console.error("Error fetching available objects:", error);
        alert("An error occurred while fetching available objects. Please try again.");
      });
  } else {
    // Continue with the API call without selecting an object
    sendOrderToServer(code, jwt, order, setSelectedCards);
  }
}
/*
function sendOrderToServer(code, jwt, order, setSelectedCards) {
  // You can replace the following fetch with your actual API call
  fetch(`/api/v1/game/play/${code}/specialOrder`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${jwt}`,
      Accept: "application/json",
    },
    body: JSON.stringify(order),
  })
    .then((response) => response.json())
    .then((response) => {
      if (response.success) {
        // Assuming the server responds with success
        // Update the selected cards or perform any other necessary actions
        getAlreadySelectedCardByPlayers(response.dwarves, setSelectedCards);
      } else {
        // Handle the case where the special order is not successful
        alert("Special order failed. Please try again.");
      }
    })
    .catch((error) => {
      console.error("Error during special order:", error);
      alert("An error occurred during the special order. Please try again.");
    });
}


function sellAnItem(code, jwt, setSelectedCards) {
  const gold = prompt("Enter the number of gold:");
  const steal = prompt("Enter the number of steal:");
  const iron = prompt("Enter the number of iron:");

  const order = {
    gold: parseInt(gold),
    steal: parseInt(steal),
    iron: parseInt(iron),
  };

  const wantsObject = window.confirm("Do you want to select an object in exchange for the materials?");
  
  if (wantsObject) {
    
    fetch(`/api/v1/game/play/${code}/availableObjects`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
      },
    })
      .then((response) => response.json())
      .then((objects) => {
        // Let the player choose an object
        const selectedObject = prompt("Select an object:\n" + objects.join("\n"));

        // Attach the selected object to the order
        order.selectedObject = selectedObject;

        // Continue with the API call
        sendSellAnItem(code, jwt, order, setSelectedCards);
      })
      .catch((error) => {
        console.error("Error fetching available objects:", error);
        alert("An error occurred while fetching available objects. Please try again.");
      });
  } else {
    // Continue with the API call without selecting an object
    sendSellAnItem(code, jwt, order, setSelectedCards);
  }
}
*/


export { 
    isFinished, sendCard,isStart
};