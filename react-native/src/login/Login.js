import React from 'react'
import { connect } from 'react-redux'
import { StyleSheet, Text, View, TextInput, TouchableOpacity } from 'react-native';

import * as actions from './LoginActions'

const Login = (props) => {
  const { loggedUser, onUsernameChange, onPasswordChange, onLogin } = props

  return (
    <View>
      <Text>Logged user: {loggedUser}</Text>
      <TextInput
        style={{height: 40, borderColor: 'gray', borderWidth: 1}}
        onChangeText={(text) => onUsernameChange(text)}
      />
      <TextInput
        style={{height: 40, borderColor: 'gray', borderWidth: 1}}
        onChangeText={(text) => onPasswordChange(text)}
      />
      <TouchableOpacity onPress={onLogin}>
        <Text>Login</Text>
      </TouchableOpacity>
    </View>
  )
}

export default connect(state => ({
    loggedUser: state.login.loggedUser
  }),
  dispatch => ({
    onUsernameChange: (username) => {
      dispatch(actions.setUsername(username))
    },
    onPasswordChange: (password) => {
      dispatch(actions.setPassword(password))
    },
    onLogin: () => {
      dispatch(actions.login())
    }
  })
)(Login)
