export const SET_USERNAME = 'SET_USERNAME'
export const SET_PASSWORD = 'SET_PASSWORD'
export const LOGIN = 'LOGIN'

export const setUsername = (username) => {
  return {
    type: SET_USERNAME,
    username
  }
}

export const setPassword = (password) => {
  return {
    type: SET_PASSWORD,
    password
  }
}

export const login = () => {
  return {
    type: LOGIN
  }
}
