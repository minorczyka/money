import React from 'react'
import { observer } from 'mobx-react/native';
import { StyleSheet, Text, View, TextInput, TouchableOpacity } from 'react-native';
import { Button, Icon, List, ListItem } from 'react-native-elements'
import { Actions } from 'react-native-router-flux';

import LoginStore from './LoginStore';

const store = new LoginStore();

const list = [
  {
    title: 'Appointments',
    icon: 'av-timer'
  },
  {
    title: 'Trips',
    icon: 'flight-takeoff'
  }
]

const Login = observer((props) => {
  const onUsernameChange = (value) => {
    store.loggedUser = value;
  }

  const onPasswordChange = (value) => {
    store.password = value;
  }

  return (
    <View style={{margin: 16, marginTop: 60}}>
      <Text>Logged user: {store.loggedUser}</Text>
      <TextInput
        style={{height: 40, borderColor: 'gray', borderWidth: 1}}
        onChangeText={(text) => onUsernameChange(text)} />
      <TextInput
        style={{height: 40, borderColor: 'gray', borderWidth: 1}}
        onChangeText={(text) => onPasswordChange(text)} />
      <Button
        raised
        small
        icon={{name: 'cached'}}
        title="Login"
        onPress={Actions.pageTwo} />
      <Icon
        reverse
        name='heartbeat'
        type='font-awesome'
        color='#f50'
        onPress={Actions.pageTwo} />
      <Icon
        raised
        name='heartbeat'
        type='font-awesome'
        color='#f50'
        onPress={() => console.log('hello')} />
      <List>
        {
          list.map((item, i) => (
            <Text
              key={i} >{item.title}</Text>
          ))
        }
      </List>
    </View>
  )
})

export default Login
