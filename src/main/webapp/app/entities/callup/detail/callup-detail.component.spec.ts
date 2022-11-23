import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CallupDetailComponent } from './callup-detail.component';

describe('Callup Management Detail Component', () => {
  let comp: CallupDetailComponent;
  let fixture: ComponentFixture<CallupDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CallupDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ callup: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CallupDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CallupDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load callup on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.callup).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
