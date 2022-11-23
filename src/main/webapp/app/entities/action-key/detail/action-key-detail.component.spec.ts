import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ActionKeyDetailComponent } from './action-key-detail.component';

describe('ActionKey Management Detail Component', () => {
  let comp: ActionKeyDetailComponent;
  let fixture: ComponentFixture<ActionKeyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActionKeyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ actionKey: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ActionKeyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ActionKeyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load actionKey on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.actionKey).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
