import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BooksSearchPage } from './books-search.page';

describe('BooksSearchPage', () => {
  let component: BooksSearchPage;
  let fixture: ComponentFixture<BooksSearchPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BooksSearchPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BooksSearchPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
