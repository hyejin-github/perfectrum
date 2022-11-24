import React from "react";
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import navLogo from "@images/logo/logo.png";

import KakaoLogin from "../assets/images/login/kakao_login_medium_narrow.png";

const REST_API_KEY = process.env.REACT_APP_REST_API_KEY;
const REDIRECT_URI = "http://j7c105.p.ssafy.io/oauth/kakao";
// const REDIRECT_URI = 'http://localhost:3000/oauth/kakao';

const KAKAO_AUTH_URI = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;

function Copyright(props) {
  return (
    <Typography variant="body2" color="text.secondary" align="center" {...props}>
      {'Copyright © '}
      <Link color="inherit" href="http://j7c105.p.ssafy.io/">
        Perfectrum
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

const theme = createTheme();


function Login() {
  return (
    <ThemeProvider theme={theme}>
      <Container style={{ border : "solid", marginTop : "100px", marginBottom : "30vh", boxShadow : "5px 5px 5px black" }} component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: '#212121' }}>
            <LockOutlinedIcon />
          </Avatar>
          {/*<Typography component="h1" variant="h5">*/}
          {/*  Sign in*/}
          {/*</Typography>*/}
          <Box sx={{display : "flex", justifyContent : "center", alignItems : "center" }}>
            <img style={{ width : "50%", margin : "10px" }} className="logo" title="!213" alt="logoImage" src={navLogo} />
          </Box>
          <Box component="form" noValidate sx={{ mt: 1 }}>
            <Box style={{ width : "100%" }} sx={{ display : "flex", justifyContent : "center", alignItems : "center", marginTop : 3, marginBottom : 2 }}>
              <a href={KAKAO_AUTH_URI} aria-label="Kakao">
                <img style={{ width : "100%" }} src={KakaoLogin} alt="" />
              </a>
            </Box>
            {/*<TextField*/}
            {/*  margin="normal"*/}
            {/*  required*/}
            {/*  fullWidth*/}
            {/*  name="password"*/}
            {/*  label="Password"*/}
            {/*  type="password"*/}
            {/*  id="password"*/}
            {/*  autoComplete="current-password"*/}
            {/*/>*/}
            {/*<FormControlLabel*/}
            {/*  control={<Checkbox value="remember" color="primary" />}*/}
            {/*  label="Remember me"*/}
            {/*/>*/}
            {/*<Button*/}
            {/*  type="submit"*/}
            {/*  fullWidth*/}
            {/*  variant="contained"*/}
            {/*  sx={{ mt: 3, mb: 2 }}*/}
            {/*>*/}
            {/*  Sign In*/}
            {/*</Button>*/}
            {/*<Grid container>*/}
            {/*  <Grid item xs>*/}
            {/*    <Link href="#" variant="body2">*/}
            {/*      Forgot password?*/}
            {/*    </Link>*/}
            {/*  </Grid>*/}
            {/*  <Grid item>*/}
            {/*    <Link href="#" variant="body2">*/}
            {/*      {"Don't have an account? Sign Up"}*/}
            {/*    </Link>*/}
            {/*  </Grid>*/}
            {/*</Grid>*/}
          </Box>
        </Box>
        <Copyright sx={{ mt: 6, mb: 4 }} />
      </Container>
    </ThemeProvider>
  );
}

export default Login;
