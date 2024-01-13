import React, { useState, useEffect } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";

function AppNavbar() {
    const [roles, setRoles] = useState([]);
    const [username, setUsername] = useState("");
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);

    const toggleNavbar = () => setCollapsed(!collapsed);

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
            setUsername(jwt_decode(jwt).sub);
        }
    }, [jwt])

    let adminLinks = <></>;
    let registeredUserLinks = <></>;
    let userLogout = <></>;
    let publicLinks = <></>;

    roles.forEach((role) => {
        if (role === "ADMIN") {
            adminLinks = (
                <>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/users">Users</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/achievements">Achievements</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/game">Games</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/">Stats</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/docs">Docs</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/rules">Rules</NavLink>
                    </NavItem>
                    
                    
                </>
            )
        }
        if (role === "USER") {
            registeredUserLinks = (
                <>
                    <li><NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/games">Games</NavLink>
                    </NavItem></li>
                    <li><NavItem>
                        <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/statistics">Stats</NavLink>
                    </NavItem></li>
                    <li><NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/leaderboard">Leaderboard</NavLink>
                    </NavItem></li>
                    <li><NavItem>
                        <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/friends">Friends</NavLink>
                    </NavItem></li>
                    <li><NavItem>
                    <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/docs">Docs</NavLink>
                     </NavItem></li>
                    <li><NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/rules">Rules</NavLink>
                    </NavItem></li>
                </>
            )
        }
    })

    if (!jwt) {
        publicLinks = (
            <>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/docs">Docs</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="register" tag={Link} to="/register">Register</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="login" tag={Link} to="/login">Login</NavLink>
                </NavItem>
            </>
        )
    } else {
        userLogout = (
            <>
                <NavItem className="d-flex">
                    <NavLink style={{ color: "white" }} id="logout" tag={Link} to="/user">{username}</NavLink>
                </NavItem>
                <NavItem className="d-flex">
                    <NavLink style={{ color: "white" }} id="logout" tag={Link} to="/logout">Logout</NavLink>
                </NavItem>
            </>
        )

    }

    return (
        <div>
            <Navbar expand="md" fixed="top" dark color="black">
                <NavbarBrand href="/">
                    <img alt="logo" src="/logo1-recortado.png" style={{ height: 40, width: 40 }} />
                    <span style={{ color: "white" }}>Dwarf</span>
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="me-auto mb-2 mb-lg-0" navbar>
                        {adminLinks}
                        {registeredUserLinks}
                    </Nav>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {publicLinks}
                        {userLogout}
                    </Nav>
                </Collapse>
            </Navbar>

        </div>
    );
}

export default AppNavbar;