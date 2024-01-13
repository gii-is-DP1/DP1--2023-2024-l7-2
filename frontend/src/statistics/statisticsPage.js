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
    totalGameTime: 0,
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

        if (achievementsData && achievementsData.message) {
          setMessage(achievementsData.message);
          setVisible(true);
        }

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

        const totalGameTimeResponse = await fetch(`/api/v1/achievements/gameTimeList/${user.username}`, {
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

        if (!totalGameTimeResponse.ok) {
          throw new Error(`HTTP error! Status: ${totalGameTimeResponse.status}`);
        }

        if (!totalGamesPlayedResponse.ok) {
          throw new Error(`HTTP error! Status: ${totalGameTimeResponse.status}`);
        }

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

        const totalGameTimeData = await totalGameTimeResponse.json();
        console.log('Total Game Time Data:', totalGameTimeData);

        const mediaData = await mediaResponse.json();
        console.log('Media Data:', mediaData);

        setStatistic((prevStatistic) => ({
          ...prevStatistic,
          victories: winnedData,
          totalMatches: gamesPlayedData,
          defeated: gamesPlayedData - winnedData, 
          totalGameTime: totalGameTimeData,
          mediaPlayersPerGame: mediaData
        }));

        setStats(achievementsData);
      } catch (error) {
        console.error('Error in fetchData:', error);
        alert(error.message);
      }
    };

    fetchData();
  }, [user.id, user.username]);  // Agrega las dependencias necesarias para useEffect

  console.log(statistic.victories);

  const filteredStats = stats.filter((achievement) => achievement.description === "stats");

const statsList = Array.isArray(filteredStats) && filteredStats.map((request) => (
  <tr key={request.id}>

    <td>{statistic.totalMatches}</td>
    <td>{statistic.victories}</td>
    <td>{statistic.defeated}</td>
    <td>{statistic.totalGameTime}</td>
    <td>{statistic.mediaPlayersPerGame}</td>
  </tr>
));

  return (
    <div className="auth-page-container" style={{ height: "100vh", marginTop: "65px", textAlign: "center" }}>
      <div className="friends" style={{ marginTop: "10px" }}>
        <h3 style={{ marginTop: "30px" }}>My Stats</h3>
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
                Total Game Time
              </th>
              <th width="15%" className="text-center" style={{ borderBottom: "2px solid black" }}>
                Media Players Per Game
              </th>
            </tr>
          </thead>
          <tbody>{statsList}</tbody>
        </table>
      </div>
    </div>
  );
};