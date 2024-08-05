import { Component } from '@angular/core';
import { RegistractionRequest } from '../../services/models/registraction-request';
import { AuthenticationService } from '../../services/services/authentication.service';

import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  registerRequest: RegistractionRequest = {
    email: '',
    firstname: '',
    lastname: '',
    password: '',
  };
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  register(): void {
    this.errorMsg = [];
    this.authService
      .register({
        body: this.registerRequest,
      })
      .subscribe({
        next: (): void => {
          this.router.navigate(['activate-account']);
        },
        error: (err): void => {
          this.errorMsg = err.error.validtionErrors;
        },
      });
  }

  login(): void {
    this.router.navigate(['login']);
  }
}
