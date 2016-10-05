import * as actions from './LoginActions'

export default function counter(state = { }, action) {
  switch (action.type) {
    case actions.SET_USERNAME:
      return {
        ...state,
        username: action.username
      }
    case actions.SET_PASSWORD:
      return {
        ...state,
        password: action.password
      }
    case actions.LOGIN:
      return {
        ...state,
        loggedUser: state.username
      }
    default:
      return state;
  }
}
