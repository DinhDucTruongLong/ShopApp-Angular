import { Routes } from '@angular/router';
import { Home } from './home/home'; // Điều chỉnh đường dẫn đến file home.component của bạn

export const routes: Routes = [
  {
    path: '', // Khi truy cập đường dẫn gốc (ví dụ: http://localhost:4200/)
    component: Home // Component Home sẽ được hiển thị
  }
];