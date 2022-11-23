import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InjuryDetailComponent } from './injury-detail.component';

describe('Injury Management Detail Component', () => {
  let comp: InjuryDetailComponent;
  let fixture: ComponentFixture<InjuryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InjuryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ injury: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InjuryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InjuryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load injury on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.injury).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
