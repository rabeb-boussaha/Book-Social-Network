import { Component } from '@angular/core';
import { Route, Router } from '@angular/router';
import {
  AuthenticationRequest,
  AuthenticationResponse,
} from 'src/app/services/models';
import { AuthenticationService } from 'src/app/services/services';
import { TokenService } from 'src/app/services/token/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  authRequest: AuthenticationRequest = { email: '', password: '' };
  errorMsg: Array<string> = [];

  // il faut injecter par constructer
  constructor(
    private router: Router,
    private authServices: AuthenticationService,
    private tokenService: TokenService
  ) // anther services
  {}

  login(): void {
    this.errorMsg = [];
    this.authServices
      .authenticate({
        body: this.authRequest,
      })
      .subscribe({
        next: (res: AuthenticationResponse): void => {
          //to do save token
          this.tokenService.token = res.token as string;
          this.router.navigate(['books']);
        },
        error: (err) => {
          console.log(err);
          if (err.error.validtionErrors) {
            this.errorMsg = err.error.validtionErrors;
          } else {
            this.errorMsg.push(err.error.error);
          }
        },
      });
  }

  register(): void {
    this.router.navigate(['register']);
  }
}
