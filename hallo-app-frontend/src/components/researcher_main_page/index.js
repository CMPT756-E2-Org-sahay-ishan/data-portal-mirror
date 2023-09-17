import React, { useState } from 'react';
import { useLocalState } from '../util/useLocalStorage';

import {
	Container,
	Header,
	Content,
	Sidebar,
	Sidenav,
	Nav,

} from "rsuite";
import ToggleButton from './util/toggle';
import SMRUDetectionsChart from '../smru_chart/detections_chart';
const ResearcherMain=()=>{
	const data =[

		{ date: '2023-07-05', time: '12:30', idstring:"hh"},
	
		{ date: '2023-07-06', time: '17:00', idstring:"jj"},
		{ date: '2023-07-07', time: '18:20', idstring:"kk"},
		{ date: '2023-07-08', time: '18:00', idstring:"ll"}
	
	];
  const[jwt, setJwt]=useLocalState("", "jwt");
  const [expand, setExpand] = useState(true);
  const [smruData, setSmruData]=useState([]);
  const smruFetch=()=>{
	fetch('api/smru/all', {
		headers:{
			"Content-Type":"application/json",
			Authorization:`Bearer ${jwt}`
		}, 
		method:"GET"
	}).then((response)=>{if(response.status===200) return response.json()}
		).then(events=> {console.log(events.slice(0,10));setSmruData(events)})
}

  
  const logout = ()=>{
    setJwt(null);
    window.location.href="/signin";

  }
  return (

    		<div>
			<Container 
				style={{
					
					height:"100%;"
					
				}}
			>
				<Sidebar
					style={{
						display: "flex",
						flexDirection: "column",
						marginLeft:"2px",
						height:"100%"
						
					}}
					width={expand ? 260 : 56}
					collapsible
				>
					<Sidenav.Header>
						<div
							style={{
								padding: 18,
								fontSize: 20,
								height: 56,
								color: " #fff",
								background: "#c1bbcb",
							}}
						>
							<span style={{ marginLeft: 12 }}>
								HALLO
							</span>
						</div>
					</Sidenav.Header>
					<Sidenav expanded={expand} defaultOpenKeys={["2"]}>
						<Sidenav.Body>
							<Nav>
								<Nav.Item eventKey="1" active 
								onClick={()=>smruFetch()}
								>
									Events
								</Nav.Item>
						
								<Nav.Menu
									eventKey="3"
									trigger="hover"
									title="Actions"
									placement="rightStart"
								>
									<Nav.Item eventKey="3-1">
										action 1
									</Nav.Item>
									<Nav.Item eventKey="3-2">
									action 2
									</Nav.Item>
									<Nav.Item eventKey="3-3">
									action 3
									</Nav.Item>
									<Nav.Item eventKey="3-4">
									action 4
									</Nav.Item>
								</Nav.Menu>
								<Nav.Item eventKey="2" onClick={()=>{logout();}}>
									Sign out
								</Nav.Item>
							</Nav>
						</Sidenav.Body>
					</Sidenav>
					<ToggleButton
						expand={expand}
						onChange={() => setExpand(!expand)}
					/>
				</Sidebar>

				<Container>
					<Header style={{ paddingLeft: 20, paddingTop:5 }}>
						<h2>Welcome to the HALLO application</h2>
					</Header>
					<Content style={{ padding: 20 }}>
						<h3>Dashboard</h3>
						<p>
						Humans and Algorithms Listening to Orcas explores the concept of training artificial intelligence systems to detect underwater whale vocalizations. The research goal is to develop a whale forecasting system to warn nearby ships of whale presence. This could prevent potentially fatal ship strikes for the endangered Southern Resident killer whales and other whale species that frequent the waters of the Salish Sea in the Pacific Northwest.
						</p>
						<div style={{'width':'700px'}}>
						{smruData.length!=0 && <SMRUDetectionsChart data={smruData}/>}
						
						</div>
						
					</Content>
				</Container>
			</Container>
		</div>

  )
}

export default ResearcherMain; 









