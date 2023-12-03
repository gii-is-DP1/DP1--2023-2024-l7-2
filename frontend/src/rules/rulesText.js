import React from 'react';
import '../static/css/rules/rules.css'; 
import { Button } from 'reactstrap';


export default function RulesText() {
  return (
    <div style={{ marginTop: "65px" }}>
        <div className="row">
          
          <div className="col">
            <div className="rules_page1" style={{height: "100vh", width: "100vw"}}>
            </div>
          </div>
          <div className="col">
            <div className="rules_page2" style={{height: "100vh", width: "100vw"}}>
            </div>
          </div>

        </div>
    </div>

  );
}
