import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MatchdayDetailComponent } from './matchday-detail.component';

describe('Matchday Management Detail Component', () => {
  let comp: MatchdayDetailComponent;
  let fixture: ComponentFixture<MatchdayDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MatchdayDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ matchday: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MatchdayDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MatchdayDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load matchday on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.matchday).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
