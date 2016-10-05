import React, {Component} from 'react';
import { createStore, combineReducers } from 'redux';
import { Provider } from 'react-redux';

import App from './app/App'
import * as reducers from './app/reducers';

const reducer = combineReducers(reducers);
const store = createStore(reducer);

export default Main = () => {
  return (
    <Provider store={store}>
      <App />
    </Provider>
  );
}
