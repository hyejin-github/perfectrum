import React, { useState } from "react";
import Box from '@mui/material/Box';
import Backdrop from '@mui/material/Backdrop';
import SpeedDial from '@mui/material/SpeedDial';
import SpeedDialIcon from '@mui/material/SpeedDialIcon';
import SpeedDialAction from '@mui/material/SpeedDialAction';
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import PersonIcon from '@mui/icons-material/Person';
import LiveHelpTwoToneIcon from "@mui/icons-material/LiveHelpTwoTone";
import FileCopyIcon from '@mui/icons-material/FileCopyOutlined';
import CloseRoundedIcon from "@mui/icons-material/CloseRounded";
import uuid from "react-uuid";
import { getAccordClass } from "../../apis/perfume";
import navLogo from "@images/logo/logo.png";
import "./Test.scss";

const actions = [
  { icon: <QuestionMarkIcon />, name: 'Info' },
  { icon: <PersonIcon />, name: 'Profile' }
];
export default function Test() {
  const [open, setOpen] = useState(false);
  const [openDoc, setOpenDoc] = useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const handleDoc = () => setOpenDoc(true);
  return (
    <Box sx={{ height: 330, transform: 'translateZ(0px)', flexGrow: 1 }} className="Box">
      <Backdrop open={open} />
      <SpeedDial
        ariaLabel="SpeedDial tooltip example"
        sx={{ position: 'absolute', bottom: 16, right: 16 }}
        icon={<SpeedDialIcon />}
        onClose={handleClose}
        onOpen={handleOpen}
        open={open}
      >
        {actions.map((action) => (
          <SpeedDialAction
            key={action.name}
            icon={action.icon}
            tooltipTitle={action.name}
            tooltipOpen
            onClick={handleDoc}
          />
        ))}
      </SpeedDial>
    </Box>
  );
}