import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookCreationPage } from './book-creation.page';

describe('BookCreationPage', () => {
  let component: BookCreationPage;
  let fixture: ComponentFixture<BookCreationPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookCreationPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookCreationPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
