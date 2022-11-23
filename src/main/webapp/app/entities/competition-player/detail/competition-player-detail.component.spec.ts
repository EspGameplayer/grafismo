import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompetitionPlayerDetailComponent } from './competition-player-detail.component';

describe('CompetitionPlayer Management Detail Component', () => {
  let comp: CompetitionPlayerDetailComponent;
  let fixture: ComponentFixture<CompetitionPlayerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompetitionPlayerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ competitionPlayer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompetitionPlayerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompetitionPlayerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load competitionPlayer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.competitionPlayer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
