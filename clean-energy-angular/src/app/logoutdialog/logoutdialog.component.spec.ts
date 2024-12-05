import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoutdialogComponent } from './logoutdialog.component';

describe('LogoutdialogComponent', () => {
  let component: LogoutdialogComponent;
  let fixture: ComponentFixture<LogoutdialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LogoutdialogComponent]
    });
    fixture = TestBed.createComponent(LogoutdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
