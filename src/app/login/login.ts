import { Component } from '@angular/core';
import { Header } from "../header/header";
import { Footer } from "../footer/footer";

@Component({
  selector: 'app-login',
    standalone: true,
    imports: [Header, Footer],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class Login {

}
