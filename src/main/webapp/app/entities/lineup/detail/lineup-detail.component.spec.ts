import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LineupDetailComponent } from './lineup-detail.component';

describe('Lineup Management Detail Component', () => {
  let comp: LineupDetailComponent;
  let fixture: ComponentFixture<LineupDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LineupDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ lineup: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LineupDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LineupDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load lineup on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.lineup).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
