import {Component, Input, signal} from '@angular/core';
import {RouterModule} from '@angular/router';

export interface NavItem {
  label: string;
  route: string;
  icon?: string; // optional icon name (Material, SVG key, etc.)
  exact?: boolean; // router exact matching
  disabled?: boolean;
}

@Component({
  standalone: true,
  selector: 'app-top-nav',
  imports: [RouterModule],
  templateUrl: './top-nav.component.html',
  styleUrl: './top-nav.component.css',
})
export class TopNavComponent {
  private readonly _items = signal<NavItem[]>([]);

  @Input({ required: true })
  set navItems(value: NavItem[]) {
    this._items.set(value);
  }

  items = this._items.asReadonly();
}
