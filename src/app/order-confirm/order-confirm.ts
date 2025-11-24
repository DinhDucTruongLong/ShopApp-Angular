import { Component } from '@angular/core';
import { Header } from "../header/header";
import { Footer } from "../footer/footer";

@Component({
  selector: 'app-order-confirm',
  standalone: true,
  imports: [Header, Footer],
  templateUrl: './order-confirm.html',
  styleUrls: ['./order-confirm.css'],
})
export class OrderConfirm {

}
