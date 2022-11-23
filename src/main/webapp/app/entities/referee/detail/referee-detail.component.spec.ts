import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RefereeDetailComponent } from './referee-detail.component';

describe('Referee Management Detail Component', () => {
  let comp: RefereeDetailComponent;
  let fixture: ComponentFixture<RefereeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RefereeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ referee: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RefereeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RefereeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load referee on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.referee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
