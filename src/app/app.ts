import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Footer } from "./footer/footer";
import { Header } from "./header/header";

@Component({
  selector: 'app-detail-product',
  standalone: true,
  imports: [Footer, Header],
  //templateUrl: './home/home.html',
  //styleUrl: './home/home.css'
  templateUrl: './detail-product/detail-product.html',
  styleUrls: ['./detail-product/detail-product.css']
})
export class App {
  protected readonly title = signal('shopapp-angular');
}
