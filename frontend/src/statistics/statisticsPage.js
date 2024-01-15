import React, { useState, useEffect } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function StatsList() {
  const user = tokenService.getUser();
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [statistic, setStatistic] = useState({
    victories: 0,
    defeated: 0,
    totalMatches: 0,
    mediaPlayersPerGame: 0,
  });

  const [stats, setStats] = useFetchState([]);
  const [achievements, setAchievements] = useFetchState([]);

  const fetchData = async () => {
    try {
      const winnedResponse = await fetch(
        `/api/v1/achievements/allWinnedGames/${user.id}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        }
      );

      const totalGamesPlayedResponse = await fetch(
        `/api/v1/achievements/allPlayedGames/${user.username}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        }
      );

      const mediaResponse = await fetch(
        `/api/v1/achievements/mediaPlayers/${user.username}`,
        {
          method: "GET",
          headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        }
      );

      if (!winnedResponse.ok) {
        throw new Error(`HTTP error! Status: ${winnedResponse.status}`);
      }

      if (!mediaResponse.ok) {
        throw new Error(`HTTP error! Status: ${mediaResponse.status}`);
      }

      const winnedData = await winnedResponse.json();
      console.log("Winned Data:", winnedData);

      const gamesPlayedData = await totalGamesPlayedResponse.json();
      console.log("Games Played Data:", gamesPlayedData);

      const mediaData = await mediaResponse.json();
      console.log("Media Data:", mediaData);

      setStatistic((prevStatistic) => ({
        ...prevStatistic,
        victories: winnedData,
        totalMatches: gamesPlayedData,
        defeated: gamesPlayedData - winnedData,
        mediaPlayersPerGame: mediaData,
      }));
    } catch (error) {
      console.error("Error in fetchData:", error);
      alert(error.message);
    }
  };

  const fetchAchievements = async () => {
    try {
      const achievementsResponse = await fetch("/api/v1/achievements", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
      });

      if (!achievementsResponse.ok) {
        throw new Error(
          `HTTP error! Status: ${achievementsResponse.status}`
        );
      }

      const achievementsData = await achievementsResponse.json();
      console.log("All Achievements Data:", achievementsData);

      // Ensure winnedData and gamesPlayedData are valid numbers
      const validWinnedData =
        typeof statistic.victories === "number" ? statistic.victories : 0;
      const validGamesPlayedData =
        typeof statistic.totalMatches === "number" ? statistic.totalMatches : 0;

      // Compare achievements with user stats
      const updatedAchievements = achievementsData
        .filter((achievement) => achievement.description !== "stats")
        .map((achievement) => {
          if (
            typeof achievement.totalVictories === "number" &&
            typeof achievement.totalMatches === "number"
          ) {
            return {
              ...achievement,
              achieved:
                achievement.totalVictories <= validWinnedData &&
                achievement.totalMatches <= validGamesPlayedData,
            };
          }
          return achievement; // Skip comparison for null values
        });

      setAchievements(updatedAchievements);
    } catch (error) {
      console.error("Error fetching all achievements:", error);
      alert(error.message);
    }
  };

  useEffect(() => {
    fetchData();
    fetchAchievements();
  }, [statistic]); // Re-run effect when statistic changes

  const nonStatsAchievementsList =
    Array.isArray(achievements) &&
    achievements.map((achievement, index) => (
      <li key={index}>
        {achievement.name} - {achievement.achieved ? "Achieved" : "Not Achieved"}
      </li>
    ));

  return (
    <div
      className="auth-page-container"
      style={{ height: "100vh", marginTop: "65px", textAlign: "center" }}
    >
      <div className="friends" style={{ marginTop: "10px" }}>
        <h3 style={{ marginTop: "30px" }}>My Stats</h3>
      </div>
      <table>
        <thead>
          <tr>
            <th
              width="15%"
              className="text-center"
              style={{ borderBottom: "2px solid black" }}
            >
              Total Matches
            </th>
            <th
              width="15%"
              className="text-center"
              style={{ borderBottom: "2px solid black" }}
            >
              Total Victories
            </th>
            <th
              width="15%"
              className="text-center"
              style={{ borderBottom: "2px solid black" }}
            >
              Total Defeats
            </th>
            <th
              width="15%"
              className="text-center"
              style={{ borderBottom: "2px solid black" }}
            >
              Media Players Per Game
            </th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{statistic.totalMatches}</td>
            <td>{statistic.victories}</td>
            <td>{statistic.defeated}</td>
            <td>{statistic.mediaPlayersPerGame}</td>
          </tr>
        </tbody>
      </table>

      <div>
        <h3>Achievements</h3>
        <ul>{nonStatsAchievementsList}</ul>
      </div>
    </div>
  );
}
