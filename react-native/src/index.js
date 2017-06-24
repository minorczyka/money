import React, {Component} from 'react';
import { Router, Scene } from 'react-native-router-flux';

import Login from './login/Login'

const Main = () => {
  return (
    <Router>
      <Scene key="root">
        <Scene key="pageOne" component={Login} title="PageOne" initial={true} />
        <Scene key="pageTwo" component={Login} title="PageTwo" />
      </Scene>
    </Router>
  );
}

export default Main
