import { Component, Input } from '@angular/core';
import {AbstractControl, FormControl, ReactiveFormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-form-field',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-field.component.html',
})
export class FormFieldComponent {
  @Input({ required: true }) label!: string;
  @Input({ required: true }) control!: FormControl;

  @Input() type: string = 'text';
  @Input() placeholder: string = '';
  @Input() errorMessage: string = '';
  @Input() inputId?: string;

  get showError(): boolean {
    return (
      this.control &&
      this.control.touched &&
      this.control.invalid
    );
  }
}
