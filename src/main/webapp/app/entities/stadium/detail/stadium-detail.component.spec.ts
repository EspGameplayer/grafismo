import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StadiumDetailComponent } from './stadium-detail.component';

describe('Stadium Management Detail Component', () => {
  let comp: StadiumDetailComponent;
  let fixture: ComponentFixture<StadiumDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StadiumDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ stadium: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StadiumDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StadiumDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stadium on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.stadium).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
