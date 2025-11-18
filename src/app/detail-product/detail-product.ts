import { Component } from '@angular/core';
import { Header } from "../header/header";
import { Footer } from "../footer/footer";

@Component({
  selector: 'app-detail-product',
  standalone: true,
  imports: [Header, Footer],
  templateUrl: './detail-product.html',
  styleUrls: ['./detail-product.css'],
})
export class DetailProduct {

}
