import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MatchStatsDetailComponent } from './match-stats-detail.component';

describe('MatchStats Management Detail Component', () => {
  let comp: MatchStatsDetailComponent;
  let fixture: ComponentFixture<MatchStatsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MatchStatsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ matchStats: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MatchStatsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MatchStatsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load matchStats on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.matchStats).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
