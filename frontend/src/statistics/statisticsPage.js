import React, { useState, useEffect } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";

import { Form, Button, Input, Label, Text  } from "reactstrap";
import { Link } from "react-router-dom";

const jwt = tokenService.getLocalAccessToken();

export default function StatsList() {
  const user = tokenService.getUser();
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [statistic, setStatistic] = useState({
    victories: 0,
    defeated: 0,
    totalMatches: 0,
    mediaPlayersPerGame: 0
  });

  const [stats, setStats] = useFetchState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {

        const achievementsResponse = await fetch(`/api/v1/achievements/all/${user.username}`, {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          }

          
        });

        if (!achievementsResponse.ok) {
          throw new Error(`HTTP error! Status: ${achievementsResponse.status}`);
        }

        const achievementsData = await achievementsResponse.json();
        console.log('Achievements Data:', achievementsData);

        const winnedResponse = await fetch(`/api/v1/achievements/allWinnedGames/${user.id}`, {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          }
        });

        const totalGamesPlayedResponse = await fetch(`/api/v1/achievements/allPlayedGames/${user.username}`, {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          }
        });

        const mediaResponse = await fetch(`/api/v1/achievements/mediaPlayers/${user.username}`, {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          }
        });

        if (!winnedResponse.ok) {
          throw new Error(`HTTP error! Status: ${winnedResponse.status}`);
        }

        if (!mediaResponse.ok) {
          throw new Error(`HTTP error! Status: ${winnedResponse.status}`);
        }

        const winnedData = await winnedResponse.json();
        console.log('Winned Data:', winnedData);

        const gamesPlayedData = await totalGamesPlayedResponse.json();
        console.log('Games Played Data:', gamesPlayedData);

        const mediaData = await mediaResponse.json();
        console.log('Media Data:', mediaData);

        setStatistic((prevStatistic) => ({
          ...prevStatistic,
          victories: winnedData,
          totalMatches: gamesPlayedData,
          defeated: gamesPlayedData - winnedData,
          mediaPlayersPerGame: mediaData
        }));

      setStats(achievementsData);

      const statsAchievement = achievementsData.find((achievement) => achievement.description === "stats");

        if (!statsAchievement) {
          const createStatsAchievementResponse = await fetch("/api/v1/achievements", {
            method: "POST",
            headers: {
              "Authorization": `Bearer ${jwt}`,
              Accept: "application/json",
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              totalMatches: 0,
              totalVictories: 0,
              totalIron: 0,
              totalGold: 0,
              totalSteal: 0,
              totalObjects: 0,
              description: "stats",
            }),
          });

          if (!createStatsAchievementResponse.ok) {
            throw new Error(`HTTP error! Status: ${createStatsAchievementResponse.status}`);
          }

          const createdStatsAchievement = await createStatsAchievementResponse.json();
          console.log('Created Stats Achievement:', createdStatsAchievement);

          // Actualizar el estado de stats con el nuevo logro creado
          setStats((prevStats) => [...prevStats, createdStatsAchievement]);
        }
        
      } catch (error) {
        console.error('Error in fetchData:', error);
        alert(error.message);
      }
    };
    function updateStatsAchievement(){
      try {
        const statsAchievement = stats.find((achievement) => achievement.description === "stats");
  
      if (!statsAchievement || !statsAchievement.id) {
        console.error('Error: ID of the achievement is undefined or null');
        return;
      }
  
      const updateStatsAchievementResponse = fetch(`/api/v1/achievements/3`, {
        method: "PUT",
        headers: {
          "Authorization": `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          id: statsAchievement.id,
          description: "stats",
          totalIron:0,
          totalGold:0,
          totalSteal:0,
          totalObjects:0,
          totalMatches: statistic.totalMatches,
          totalVictories: statistic.victories,
          totalGameTime: statistic.totalGameTime
          // Puedes agregar más campos aquí según tus necesidades
        }),
      });
  
      if (!updateStatsAchievementResponse.ok) {
        throw new Error(`HTTP error! Status: ${updateStatsAchievementResponse.status}`);
      }
  
      console.log(statsAchievement)
  
      } catch (error) {
        console.error('Error updating Stats Achievement:', error);
        alert(error.message);
      }
    };

    updateStatsAchievement()
    fetchData();
  }, []);

  console.log(statistic)

  const filteredStats = stats.filter((achievement) => achievement.description === "stats");

  const statsList = Array.isArray(filteredStats) && filteredStats.map((request, index) => (
    <tr key={index}>
      <td>{statistic.totalMatches}</td>
      <td>{statistic.victories}</td>
      <td>{statistic.defeated}</td>
      <td>{statistic.mediaPlayersPerGame}</td>
    </tr>
  ));


  return (
    <div className="auth-page-container" style={{ height: "100vh", marginTop: "65px", textAlign: "center" }}>
      <div className="friends" style={{ marginTop: "10px" }}>
        <h3 style={{ marginTop: "30px" }}>My Stats</h3>
      </div>
      <table>
          <thead>
            <tr>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Total Matches
              </th>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Total Victories
              </th>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Total Defeates
              </th>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Media Players Per Game
              </th>
            </tr>
          </thead>
          <tbody>{statsList[0]}</tbody>
        </table>
    </div>
  );
};