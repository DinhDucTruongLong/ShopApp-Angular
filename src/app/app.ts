import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Footer } from "./footer/footer";
import { Header } from "./header/header";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [Footer],
  //templateUrl: './home/home.html',
  //styleUrl: './home/home.css'
  // templateUrl: './detail-product/detail-product.html',
  // styleUrls: ['./detail-product/detail-product.css']
  // templateUrl: './order/order.html',
  // styleUrls: ['./order/order.css']
  // templateUrl: './order-confirm/order-confirm.html',
  // styleUrls: ['./order-confirm/order-confirm.css']
  //  templateUrl: './login/login.html',
  // styleUrls: ['./login/login.css']
  templateUrl: './register/register.html',
  styleUrl: './register/register.css',

})
export class App {
  protected readonly title = signal('shopapp-angular');
}
