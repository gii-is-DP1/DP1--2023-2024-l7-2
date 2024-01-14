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


function resign(code, jwt) {
  fetch(`/api/v1/game/play/${code}/resign`, {
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

function checkResourcesSelectCard(cards, player) {
  let totalGold = 0;
  let totalIron = 0;
  let totalSteal = 0;
  for (const card in cards) {
    if (cards[card].totalGold < 1)
      totalGold += cards[card].totalGold

    if (cards[card].totalGold < 1)
      totalIron += cards[card].totalGold

    if (cards[card].totalSteal < 1)
      totalSteal += cards[card].totalSteal
  }
  if (totalGold * -1 > player.gold || totalIron * -1 > player.iron 
    || totalSteal * -1 > player.steal) {
    return false;
  }

  return true;
}


export { 
    isFinished, sendCard,isStart, resign, checkResourcesSelectCard
};