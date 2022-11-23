import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PositionDetailComponent } from './position-detail.component';

describe('Position Management Detail Component', () => {
  let comp: PositionDetailComponent;
  let fixture: ComponentFixture<PositionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PositionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ position: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PositionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PositionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load position on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.position).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
