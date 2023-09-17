
import {

	Nav,
	Navbar,
} from "rsuite";

import "rsuite/dist/rsuite.min.css";
import Cog from "@rsuite/icons/legacy/Cog";
import AngleLeft from "@rsuite/icons/legacy/AngleLeft";
import AngleRight from "@rsuite/icons/legacy/AngleRight";

const ToggleButton = ({ expand, onChange }) => {
	return (
		<Navbar appearance="subtle" className="nav-toggle">
			<Navbar.Body>
				<Nav>
					<Nav.Menu
						placement="topStart"
						trigger="click"
						renderTitle={(children) => {
							return (
								<Cog
									style={{
										width: 56,
										height: 56,
										padding: 18,
										lineHeight: "56px",
										textAlign: "center",
									}}
								/>
							);
						}}
					>
						<Nav.Item>Help</Nav.Item>
						<Nav.Item>Settings</Nav.Item>
						<Nav.Item>Sign out</Nav.Item>
					</Nav.Menu>
				</Nav>

				<Nav pullRight>
					<Nav.Item
						onClick={onChange}
						style={{ width: 56, textAlign: "center" }}
					>
						{expand ? <AngleLeft /> : <AngleRight />}
					</Nav.Item>
				</Nav>
			</Navbar.Body>
		</Navbar>
	);
};

export default ToggleButton; 