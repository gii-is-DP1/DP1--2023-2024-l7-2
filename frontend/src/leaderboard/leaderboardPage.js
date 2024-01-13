import React, { useState, useEffect } from "react";
import tokenService from "../services/token.service";

export default function LeaderboardList() {
  const jwt = tokenService.getLocalAccessToken();
  const user = tokenService.getUser();
  const [stats, setStats] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const usersResponse = await fetch(`/api/v1/users`, {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        });

        if (!usersResponse.ok) {
          throw new Error(`HTTP error! Status: ${usersResponse.status}`);
        }

        const usersData = await usersResponse.json();
        console.log('Users Data:', usersData);

        // Hacer llamadas a la API para cada usuario
        const promises = usersData.map(async user => {
          const victoriesResponse = await fetch(`/api/v1/achievements/allWinnedGames/${user.id}`, {
            method: "GET",
            headers: {
              "Authorization": `Bearer ${jwt}`,
              Accept: "application/json",
              "Content-Type": "application/json",
            },
          });

          if (!victoriesResponse.ok) {
            throw new Error(`HTTP error! Status: ${victoriesResponse.status}`);
          }

          const victoriesData = await victoriesResponse.json();
          return { user, victoriesData }; // Combina datos de usuario y estadísticas de victorias
        });

        const statsDataArray = await Promise.all(promises);

        // Ordenar las estadísticas por número de victorias en orden descendente
        statsDataArray.sort((a, b) => b.victoriesData - a.victoriesData);

        // Actualizar el estado stats con la información de las estadísticas
        setStats(statsDataArray);
      } catch (error) {
        console.error('Error in fetchData:', error);
        alert(error.message);
      }
    };

    fetchData();
  }, [user.id, user.username]);

  console.log(stats);

  const statsList = stats.map((data, index) => (
    <tr key={index}>
      <td>{data.user.username}</td>
      <td>{data.victoriesData}</td>
    </tr>
  ));

  return (
    <div className="auth-page-container" style={{ height: "100vh", marginTop: "65px", textAlign: "center" }}>
      <div className="friends" style={{ marginTop: "10px" }}>
        <h3 style={{ marginTop: "30px" }}>LeaderBoard</h3>
        <table>
          <thead>
            <tr>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                User
              </th>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Total Victories
              </th>
            </tr>
          </thead>
          <tbody>
            {statsList}
          </tbody>
        </table>
      </div>
    </div>
  );
};
