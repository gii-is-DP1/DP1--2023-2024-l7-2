import React from "react";
import { Route, Routes } from "react-router-dom";
import jwt_decode from "jwt-decode";
import { ErrorBoundary } from "react-error-boundary";
import AppNavbar from "./AppNavbar";
import Home from "./home";
import PrivateRoute from "./privateRoute";
import Register from "./auth/register";
import Login from "./auth/login";
import Logout from "./auth/logout";
import PlanList from "./public/plan";
import tokenService from "./services/token.service";
import UserListAdmin from "./admin/users/UserListAdmin";
import UserEditAdmin from "./admin/users/UserEditAdmin";
import SwaggerDocs from "./public/swagger";
import AchievementList from "./achievements/achievementList";
import AchievementEdit from "./achievements/achievementEdit";

import RulesText from "./rules/rulesText";
import UserPage from "./user/userPage";


import GameList from "./game/gameList";
import GameEdit from "./game/gameEdit";
import GameJoin from "./game/gameJoin";
import GamePlay from "./game/gamePlay";
import GameEnd from "./game/gameEnd";

import CardViewer from "./cards/cardViewer";
import CardList from "./cards/cardList";




function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  )
}

function App() {
  const jwt = tokenService.getLocalAccessToken();
  let roles = []
  if (jwt) {
    roles = getRolesFromJWT(jwt);
  }

  function getRolesFromJWT(jwt) {
    return jwt_decode(jwt).authorities;
  }

  let adminRoutes = <></>;
  let registeredUserRoutes = <></>;
  let userRoutes = <></>;
  let publicRoutes = <></>;

  roles.forEach((role) => {
    if (role === "ADMIN") {
      adminRoutes = (
        <>
          <Route path="/users" exact={true} element={<PrivateRoute><UserListAdmin /></PrivateRoute>} />
          <Route path="/users/:username" exact={true} element={<PrivateRoute><UserEditAdmin /></PrivateRoute>} />
          <Route path="/achievements/" exact={true} element={<PrivateRoute><AchievementList /></PrivateRoute>} />
          <Route path="/achievements/:achievementId" exact={true} element={<PrivateRoute><AchievementEdit/></PrivateRoute>} />
          <Route path="/game/" exact={true} element={<PrivateRoute><GameList/></PrivateRoute>} />
          <Route path="/game/edit/:gameId" exact={true} element={<PrivateRoute><GameEdit/></PrivateRoute>} />
          <Route path="/game/edit/" exact={true} element={<PrivateRoute><GameEdit/></PrivateRoute>} />
          <Route path="/card" exact={true} element={<PrivateRoute><CardViewer/></PrivateRoute>} />
          <Route path="/cards" exact={true} element={<PrivateRoute><CardList/></PrivateRoute>}/>
          <Route path="/rules" exact={true} element={<PrivateRoute><RulesText/></PrivateRoute>}/>
          <Route path="/user" exact={true} element={<PrivateRoute><UserPage/></PrivateRoute>}/>
          
          
          
        </>)
    }
    if (role === "USER") {
      registeredUserRoutes = (
        <>
          <Route path="/game/" exact={true} element={<PrivateRoute><GameJoin/></PrivateRoute>} />
          <Route path="/game/edit/:gameId" exact={true} element={<PrivateRoute><GameEdit/></PrivateRoute>} />
          <Route path="/game/edit/" exact={true} element={<PrivateRoute><GameEdit/></PrivateRoute>} />
          <Route path="/game/:gameId" exact={true} element={<PrivateRoute><GamePlay/></PrivateRoute>} />

          <Route path="/rules" exact={true} element={<PrivateRoute><RulesText/></PrivateRoute>}/>
          <Route path="/user" exact={true} element={<PrivateRoute><UserPage/></PrivateRoute>}/>

          <Route path="/game/:gameId/finish" exact={true} element={<PrivateRoute><GameEnd/></PrivateRoute>} />
         

          

        </>)
    }
  })
  if (!jwt) {
    publicRoutes = (
      <>        
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </>
    )
  } else {
    userRoutes = (
      <>
        {/* <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} /> */}        
        <Route path="/logout" element={<Logout />} />
        <Route path="/login" element={<Login />} />
      </>
    )
  }

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback} >
        <AppNavbar />
        <Routes>
          <Route path="/" exact={true} element={<Home />} />
          <Route path="/plans" element={<PlanList />} />
          <Route path="/docs" element={<SwaggerDocs />} />
          {publicRoutes}
          {userRoutes}
          {adminRoutes}
          {registeredUserRoutes}
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;
