import { Component } from '@angular/core';
import { Route, Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/services';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.scss'],
})
export class ActivateAccountComponent {
  message = '';
  isOkay = true;
  submitted = false;

  // injection par constucture
  constructor(
    private router: Router,
    private authService: AuthenticationService
  ) {}

  private confirmeAccount(token: string) {
    this.authService
      .confirm({
        token,
      })
      .subscribe({
        next: () => {
          this.message =
            'Your account has been successfully activated.\nNow you can proceed to login';
          this.submitted = true;
          this.isOkay = true;
        },
        error: (): void => {
          this.message = 'Token has been expired or invalid';
          this.submitted = true;
          this.isOkay = false;
        },
      });
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  onCodeCompleted(token: string): void {
    this.confirmeAccount(token);
  }
}
