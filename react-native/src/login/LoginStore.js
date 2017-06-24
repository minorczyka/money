import { observable, computed } from 'mobx';

class LoginStore {
  @observable loggedUser = "";
  @observable password = "";
}

export default LoginStore
