import { Component } from '@angular/core';
import { Footer } from '../footer/footer';
import { Header } from '../header/header';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [Header, Footer],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class Home {

}
