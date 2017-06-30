import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AitPageNotFoundComponent } from './ait-page-not-found.component';

describe('AitPageNotFoundComponent', () => {
  let component: AitPageNotFoundComponent;
  let fixture: ComponentFixture<AitPageNotFoundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AitPageNotFoundComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AitPageNotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
