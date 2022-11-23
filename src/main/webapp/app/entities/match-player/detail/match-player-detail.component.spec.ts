import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MatchPlayerDetailComponent } from './match-player-detail.component';

describe('MatchPlayer Management Detail Component', () => {
  let comp: MatchPlayerDetailComponent;
  let fixture: ComponentFixture<MatchPlayerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MatchPlayerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ matchPlayer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MatchPlayerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MatchPlayerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load matchPlayer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.matchPlayer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
