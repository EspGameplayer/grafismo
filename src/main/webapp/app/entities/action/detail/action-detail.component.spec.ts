import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ActionDetailComponent } from './action-detail.component';

describe('Action Management Detail Component', () => {
  let comp: ActionDetailComponent;
  let fixture: ComponentFixture<ActionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ action: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ActionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ActionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load action on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.action).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
